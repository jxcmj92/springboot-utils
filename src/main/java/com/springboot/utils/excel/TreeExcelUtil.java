package com.springboot.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TreeExcelUtil {

    /**
     * 得到一颗颗树
     */
    public static List<TreeNode> getTrees(List<List<String>> lists){
        List<TreeNode> treeNodes = getTreeNodes(lists);
        if(CollectionUtils.isEmpty(treeNodes)){
            return Collections.emptyList();
        }

        Map<String, List<TreeNode>> parentIdMap = treeNodes.stream()
                .filter(a -> !StringUtils.isEmpty(a.getParentId()))
                .collect(Collectors.groupingBy(TreeNode::getParentId));

        treeNodes.forEach(treeNode -> {
            List<TreeNode> childrenNodes = parentIdMap.get(treeNode.getId());
            if(childrenNodes != null){
                treeNode.setChildren(childrenNodes);
            }
        });

        return treeNodes.stream().filter(a -> StringUtils.isEmpty(a.getParentId())).collect(Collectors.toList());
    }

    /**
     * 将树平铺后得到的数据
     */
    public static List<TreeNode> getTreeNodes(List<List<String>> lists){
        if(CollectionUtils.isEmpty(lists)){
            return Collections.emptyList();
        }

        List<TreeNode> treeNodes = new ArrayList<>();
        for (int i = 1; i < lists.size(); i++) {
            List<String> params = lists.get(i);
            //根节点不能为空
            if (StringUtils.isEmpty(params.get(0))) {
                continue;
            }
            for (int j = 0; j < params.size(); j++) {
                String currentParam = params.get(j);
                int level = j + 1;
                if(!StringUtils.isEmpty(currentParam)){
                    TreeNode treeNode = new TreeNode();
                    treeNode.setName(currentParam);
                    treeNode.setLevel(level);
                    treeNode.setParentId(j == 0 ? null : treeNodes.get(treeNodes.size() - 1).getId());
                    treeNode.setId(DigestUtils.md5Hex(treeNode.toString()));
                    treeNodes.add(treeNode);
                }
            }
        }

        //去重
        treeNodes = treeNodes.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TreeNode::getId))),
                        ArrayList::new));

        return treeNodes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TreeNode {

        // id
        private String id;

        // 名称
        private String name;

        //父ID
        private String parentId;

        //层级
        private int level;

        private List<TreeNode> children;
    }
}
