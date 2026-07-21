package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.response.PostDTO;
import com.group2.blogplatform.entity.Topic;
import com.group2.blogplatform.repository.PostRepository;
import com.group2.blogplatform.repository.SavedPostRepository;
import com.group2.blogplatform.repository.TopicRepository;
import com.group2.blogplatform.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final SavedPostRepository savedPostRepository;

    @Override
    public List<Topic> findAllWithActive() {
        return topicRepository.getAllTopicsWithActive();
    }

    @Override
    public List<Topic> findAll() {
        return topicRepository.getAllTopics();
    }

    // --- BỔ SUNG: Phân trang tất cả Topic cho Admin ---
    @Override
    public Page<Topic> findAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    @Override
    public List<PostDTO> findAllPostsByTopic(Long currentPage, Long topicId) {
        List<PostDTO> list = postRepository.getPostByTopicID(topicId)
                .stream()
                .map(post ->
                        new PostDTO().builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .isPinned(post.isPinned())
                                .username(post.getUser().getUsername())
                                .duration(calculateDuration(post.getCreatedAt()))
                                .topicName(post.getTopic().getName())
                                .build())
                .toList();
        return list;
    }

    private String calculateDuration(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long hours = duration.toHours();

        if (hours <= 0) {
            long minutes = duration.toMinutes();
            if (minutes <= 1) return "Just now";
            return String.format("%s minutes ago", minutes);
        }

        if (hours == 1) {
            return "1 hour ago";
        }
        return String.format("%s hours ago", hours);
    }

    private boolean checkSaved(Long userId, Long postId) {
        return savedPostRepository.checkSavedByUserIdAndPostId(userId, postId);
    }

    // Hàm hỗ trợ tạo Slug chuẩn Tiếng Việt
    private String generateSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        // 1. Thay đ/Đ thành d/D
        String normalizedInput = input.replace('đ', 'd').replace('Đ', 'D');

        // 2. Tách dấu tiếng Việt
        String nowhitespace = Pattern.compile("[\\s+]").matcher(normalizedInput).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);

        // 3. Xóa các dấu sau khi tách
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String slug = pattern.matcher(normalized).replaceAll("");

        // 4. Lọc ký tự đặc biệt, chuyển chữ thường và xử lý dấu gạch ngang
        return Pattern.compile("[^\\w-]")
                .matcher(slug)
                .replaceAll("")
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    @Override
    public void saveTopic(Topic topic) {
        if (topic.getName() != null) {
            topic.setSlug(generateSlug(topic.getName()));
        }
        topicRepository.save(topic);
    }

    @Override
    public Topic getTopicById(Integer id) {
        Topic topic = topicRepository.findByID(id);
        if (topic == null) {
            throw new IllegalArgumentException("Topic not found with ID: " + id);
        }
        return topic;
    }

    @Override
    public void deleteTopic(Integer id) {
        topicRepository.deleteById(id);
    }

    // Triển khai hàm tìm kiếm theo tên hoặc mô tả
    @Override
    public List<Topic> searchTopics(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return topicRepository.getAllTopics();
        }
        String lowerKey = keyword.toLowerCase().trim();
        return topicRepository.getAllTopics().stream()
                .filter(t -> (t.getName() != null && t.getName().toLowerCase().contains(lowerKey))
                        || (t.getDescription() != null && t.getDescription().toLowerCase().contains(lowerKey)))
                .collect(Collectors.toList());
    }

    // --- BỔ SUNG: Tìm kiếm Topic có phân trang cho Admin ---
    @Override
    public Page<Topic> searchTopics(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return topicRepository.findAll(pageable);
        }
        return topicRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword.trim(), keyword.trim(), pageable);
    }
}