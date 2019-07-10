package com.springboot.utils.ireport.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-4-22 18:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Model2Entry extends BaseEntry{

    private ModelEntry modelEntries;
}
