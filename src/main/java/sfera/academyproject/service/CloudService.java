package sfera.academyproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CloudService {

    @Value("${cloud.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String uploadFile(MultipartFile file) throws IOException {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/"))
            throw new IllegalArgumentException("Faqat rasm yuklash mumkin");
        if (file.getSize() > 5 * 1024 * 1024)
            throw new IllegalArgumentException("Fayl hajmi 5MB dan katta bo'lishi mumkin emas");

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.path("data").path("url").asText();
    }
}
