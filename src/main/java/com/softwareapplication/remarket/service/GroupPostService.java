package com.softwareapplication.remarket.service;

import com.softwareapplication.remarket.domain.GroupPost;
import com.softwareapplication.remarket.dto.GroupPostDto;
import com.softwareapplication.remarket.repository.GroupPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupPostService {

    private GroupPostRepository groupPostRepository;

    public GroupPostService(GroupPostRepository groupPostRepository){
        this.groupPostRepository = groupPostRepository;
    }
    @Transactional
    public Long savePost(GroupPostDto groupPostDto){
        return groupPostRepository.save(groupPostDto.toEntity()).getId();
    }

    @Transactional
    public GroupPostDto findPost(Long id){
        GroupPost groupPost = groupPostRepository.findGroupPostById(id);
        GroupPostDto groupPostDto = new GroupPostDto(groupPost);
        return groupPostDto;
    }

    @Transactional
    public Long updatePost(GroupPostDto groupPostDto){
        return groupPostRepository.save(groupPostDto.toEntity()).getId();
    }

    @Transactional
    public void deletePost(Long id){
        groupPostRepository.deleteById(id);
    }

    @Transactional
    public List<GroupPostDto> getGroupPostList(){
        List<GroupPost> groupPostList = groupPostRepository.findAll();
        List<GroupPostDto> groupPostDtoList = new ArrayList<>();
        for(GroupPost groupPost : groupPostList){
            GroupPostDto groupPostDto = new GroupPostDto(groupPost);
            groupPostDtoList.add(groupPostDto);
        }
        return groupPostDtoList;
    }
}
