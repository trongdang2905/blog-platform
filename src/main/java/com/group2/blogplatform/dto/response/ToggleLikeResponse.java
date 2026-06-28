package com.group2.blogplatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToggleLikeResponse {
    private boolean success;
    private boolean liked;
    private long likeCount;
    private String message;
}
