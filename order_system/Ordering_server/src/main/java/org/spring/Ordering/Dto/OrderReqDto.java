package org.spring.Ordering.Dto;

import lombok.Data;

import java.util.List;
@Data
public class OrderReqDto {

    private Long itemId;
    private int count;

}

//예시데이터
/* "orderingReqDtos" : [
    {"itemId": 1, "count": 10},
    {"itemId": 2, "count": 20}
    ]
{
    "itemIds" : [1,2], "counts": [10,20]
}
 */
