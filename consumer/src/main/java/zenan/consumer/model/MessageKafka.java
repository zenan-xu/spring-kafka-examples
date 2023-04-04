package zenan.consumer.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class MessageKafka<T> {

    private String type;
    private T data;
}
