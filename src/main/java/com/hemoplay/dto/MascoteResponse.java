package com.hemoplay.dto;

import java.util.List;

public record MascoteResponse(
        VisualMascoteResponse atual,
        List<VisualMascoteResponse> visuais
) {
}
