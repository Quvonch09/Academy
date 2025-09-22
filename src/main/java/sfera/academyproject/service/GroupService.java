package sfera.academyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sfera.academyproject.dto.ApiResponse;
import sfera.academyproject.dto.request.ReqGroup;
import sfera.academyproject.dto.response.ResGroup;
import sfera.academyproject.dto.response.ResPageable;
import sfera.academyproject.entity.Group;
import sfera.academyproject.entity.Room;
import sfera.academyproject.entity.User;
import sfera.academyproject.entity.enums.Role;
import sfera.academyproject.exception.DataNotFoundException;
import sfera.academyproject.mapper.GroupMapper;
import sfera.academyproject.repository.GroupRepository;
import sfera.academyproject.repository.RoomRepository;
import sfera.academyproject.repository.UserRepository;
import sfera.academyproject.security.CustomUserDetails;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GroupMapper groupMapper;

    public ApiResponse<String> saveGroup(ReqGroup reqGroup){
        User teacher = userRepository.findById(reqGroup.getTeacherId()).orElseThrow(
                () -> new DataNotFoundException("Teacher topilmadi")
        );

        Room room = roomRepository.findById(reqGroup.getRoomId()).orElseThrow(
                () -> new DataNotFoundException("Room topilmadi")
        );

        List<String> weekdays = reqGroup.getWeekDays().stream().map(
                Enum::toString
        ).toList();

        LocalTime startTime = LocalTime.parse(reqGroup.getStartTime());
        LocalTime endTime = LocalTime.parse(reqGroup.getEndTime());
        boolean exists = groupRepository.existsByGroup(weekdays, room.getId(), startTime, endTime);
        if(exists){
            return ApiResponse.error("There is no room for the group at this time");
        }

        Group group = Group.builder()
                .name(reqGroup.getName())
                .startTime(startTime)
                .endTime(endTime)
                .room(room)
                .teacher(teacher)
                .weekDays(reqGroup.getWeekDays())
                .build();
        groupRepository.save(group);
        return ApiResponse.success(null, "Successfully saved group");
    }


    public ApiResponse<String> updateGroup(Long id, ReqGroup reqGroup){
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Group not found")
        );

        Room room = roomRepository.findById(reqGroup.getRoomId()).orElseThrow(
                () -> new DataNotFoundException("Room topilmadi")
        );

        User teacher = userRepository.findById(reqGroup.getTeacherId()).orElseThrow(
                () -> new DataNotFoundException("Teacher topilmadi")
        );

        LocalTime startTime = LocalTime.parse(reqGroup.getStartTime());
        LocalTime endTime = LocalTime.parse(reqGroup.getEndTime());
        List<String> weekdays = reqGroup.getWeekDays().stream().map(
                Enum::toString
        ).toList();

        boolean b = groupRepository.existsByGroupForUpdate(weekdays, room.getId(), startTime, endTime, group.getId());
        if (b){
            return ApiResponse.error("There is no room for the group at this time");
        }

        group.setStartTime(startTime);
        group.setEndTime(endTime);
        group.setWeekDays(reqGroup.getWeekDays());
        group.setRoom(room);
        group.setName(reqGroup.getName());
        group.setTeacher(teacher);
        groupRepository.save(group);
        return ApiResponse.success(null, "Successfully updated group");
    }



    public ApiResponse<String> deleteGroup(Long id){
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Group not found")
        );
        groupRepository.delete(group);
        return ApiResponse.success(null, "Successfully deleted group");
    }




    public ApiResponse<ReqGroup> getGroupById(Long id){
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Group not found")
        );
        return ApiResponse.success(groupMapper.toDto(group), "Success");
    }




    public ApiResponse<ResPageable> searchGroup(CustomUserDetails userDetails, String name, String teacherName,
                                                String roomName, int page, int size){
        Page<Group> groups;

        if (userDetails.getRole().equals(Role.TEACHER.name())){
            groups = groupRepository.searchByGroup(name, userDetails.getFullName(), roomName, PageRequest.of(page, size));
        } else {
            groups = groupRepository.searchByGroup(name, teacherName, roomName, PageRequest.of(page, size));
        }

        if(groups.getTotalElements() == 0){
           return ApiResponse.error("Group not found");
        }

        List<ResGroup> list = groups.stream().map(groupMapper::toDtoRes).toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(groups.getTotalElements())
                .totalPage(groups.getTotalPages())
                .body(list)
                .build();
        return ApiResponse.success(resPageable, "Success");
    }


    public ApiResponse<List<ResGroup>> getAllGroup(){
        List<Group> groups = groupRepository.findAll();
        List<ResGroup> list = groups.stream().map(groupMapper::toDtoRes).toList();
        return ApiResponse.success(list, "Success");
    }
}
