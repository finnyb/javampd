package org.bff.javampd.art;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MPDArtwork {
    private String name;
    private String path;
    private byte[] bytes;
}
