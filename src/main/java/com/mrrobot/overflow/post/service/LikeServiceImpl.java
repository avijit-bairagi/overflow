package com.mrrobot.overflow.post.service;

import com.mrrobot.overflow.common.entity.Notification;
import com.mrrobot.overflow.common.exception.AlreadyExitsException;
import com.mrrobot.overflow.common.exception.NotFoundException;
import com.mrrobot.overflow.common.repository.NotificationRepository;
import com.mrrobot.overflow.common.utils.ResponseStatus;
import com.mrrobot.overflow.post.entity.Like;
import com.mrrobot.overflow.post.repository.LikeRepository;
import com.mrrobot.overflow.profile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public Like save(Like like) throws AlreadyExitsException {

        if (likeRepository.findByLikedByAndPostId(like.getLikedBy(), like.getPost().getId()).isPresent()) {
            throw new AlreadyExitsException(ResponseStatus.ALREADY_LIKED.value(), "Already liked!");
        }

        if (userService.getUserData().getUserId() != like.getLikedBy()) {
            Notification notification = new Notification();
            notification.setUserId(like.getPost().getPostedBy());
            notification.setPostId(like.getPost().getId());
            notification.setText("<b><i>" + userService.getUserData().getUsername() + "</i></b> unfollowed on your post.");

            if (like.getPost().getGroupId() != 0) {
                notification.setGroupId(like.getPost().getGroupId());
                notification.setGroup(true);
            } else {
                notification.setGroupId(0l);
                notification.setGroup(false);
            }

            notificationRepository.save(notification);
        }

        return likeRepository.save(like);
    }

    @Override
    public List<Like> findByPostId(Long postId) {
        return likeRepository.findByPostId(postId);
    }

    @Override
    public void delete(Long userId, long postId) throws NotFoundException {
        Like like = likeRepository.findByLikedByAndPostId(userId, postId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.NOT_FOUND.value(), ResponseStatus.NOT_FOUND.description()));


        if (userId != like.getLikedBy()) {
            Notification notification = new Notification();
            notification.setUserId(like.getPost().getPostedBy());
            notification.setPostId(like.getPost().getId());
            notification.setText("<b><i>" + userService.getUserData().getUsername() + "</i></b> unfollowed on your post.");

            if (like.getPost().getGroupId() != 0) {
                notification.setGroupId(like.getPost().getGroupId());
                notification.setGroup(true);
            } else {
                notification.setGroupId(0l);
                notification.setGroup(false);
            }

            notificationRepository.save(notification);
        }


        likeRepository.delete(like);
    }
}
