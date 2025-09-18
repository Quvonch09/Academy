package sfera.academyproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.MarkDTO;
import sfera.academyproject.dto.request.ReqMark;
import sfera.academyproject.dto.response.ResMark;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.entity.User;
import sfera.academyproject.security.CustomUserDetails;
import sfera.academyproject.service.MarkService;

import java.util.List;

@RestController
@RequestMapping("/mark")
@RequiredArgsConstructor
public class MarkController {
    private final MarkService markService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "O'quvchilarni baholash")
    public ResponseEntity<ApiResponse<String>> addMark(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody ReqMark reqMark
    ) {
        return ResponseEntity.ok(markService.addMark(currentUser, reqMark));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Markni update qilish")
    public ResponseEntity<ApiResponse<String>> updateMark(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody ReqMark reqMark
    ) {
        return ResponseEntity.ok(markService.updateMark(id, currentUser, reqMark));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Markni o'chirish uchun")
    public ResponseEntity<ApiResponse<String>> deleteMark(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(markService.deleteMark(currentUser, id));
    }



    @GetMapping("/myMarks")
    @Operation(summary = "O'ziga quyilgan baholarni ko‘rish",
            description = "Teacher bo‘lsa o‘zi qo‘ygan baholar, Student yoki Parent bo‘lsa o‘ziga berilgan baholar")
    @PreAuthorize("hasAnyRole('TEACHER','STUDENT','PARENT')")
    public ResponseEntity<ApiResponse<List<ResMark>>> getMyMarks(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(markService.getMyMarks(currentUser));
    }



    @GetMapping("/search")
    @Operation(summary = "Admin barchani bahosini kurish uchun")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ResPageable>> searchMark(@RequestParam(required = false) String studentName,
                                                               @RequestParam(required = false) String teacherName,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(markService.getAllMarks(studentName,teacherName,page,size));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Markni bittasini kurish")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<MarkDTO>> getMark(@PathVariable Long id) {
        return ResponseEntity.ok(markService.getOneMark(id));
    }
}
