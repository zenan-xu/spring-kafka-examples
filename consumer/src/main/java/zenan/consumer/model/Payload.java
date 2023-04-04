package zenan.consumer.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Payload {
    private String id;
    private String info;
}
