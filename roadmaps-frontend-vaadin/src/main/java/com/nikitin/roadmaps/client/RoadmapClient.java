package com.nikitin.roadmaps.client;

import com.nikitin.roadmaps.config.security.KeycloakTokenService;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RoadmapClient extends Client {
    public RoadmapClient(RestTemplate restTemplate, KeycloakTokenService keycloakTokenService) {
        super(restTemplate, keycloakTokenService);
    }

    public ResponseEntity<String> create(RoadmapRequestDto roadmapRequestDto, Boolean notificationError) {
        return request("/roadmaps",
                HttpMethod.POST,
                buildRequestBody(roadmapRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> createChapter(Long roadmapId, RoadmapChapterRequestDto roadmapChapterRequestDto, Boolean notificationError) {
        return request("/roadmaps/" + roadmapId + "/chapter",
                HttpMethod.POST,
                buildRequestBody(roadmapChapterRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> createTopic(Long chapterId, RoadmapTopicRequestDto roadmapTopicRequestDto, Boolean notificationError) {
        return request("/roadmaps/chapter/" + chapterId + "/topic",
                HttpMethod.POST,
                buildRequestBody(roadmapTopicRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> createQuestion(Long topicId, RoadmapQuestionRequestDto roadmapQuestionRequestDto, Boolean notificationError) {
        return request("/roadmaps/topic/" + topicId + "/question",
                HttpMethod.POST,
                buildRequestBody(roadmapQuestionRequestDto, null),
                notificationError);
    }

    public ResponseEntity<String> getById(Long id, Boolean notificationError) {
        return request("/roadmaps/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAllByProfileId(Long id, Boolean notificationError) {
        return request("/roadmaps/profile/" + id,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAllTopicByChapterId(Long chapterId, Boolean notificationError) {
        return request("/roadmaps/topic/" + chapterId,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> getAllQuestionByTopicId(Long topicId, Boolean notificationError) {
        return request("/roadmaps/question/" + topicId,
                HttpMethod.GET,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteById(Long id, Boolean notificationError) {
        return request("/roadmaps/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteChapterById(Long id, Boolean notificationError) {
        return request("/roadmaps/chapter/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteTopicById(Long id, Boolean notificationError) {
        return request("/roadmaps/topic/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }

    public ResponseEntity<String> deleteQuestionById(Long id, Boolean notificationError) {
        return request("/roadmaps/question/" + id,
                HttpMethod.DELETE,
                buildRequestBody(null, null),
                notificationError);
    }
}
