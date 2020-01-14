package excel;

import com.alibaba.fastjson.JSON;
import com.springboot.utils.Application;
import com.springboot.utils.excel.PoiExcelUtil;
import com.springboot.utils.excel.TreeExcelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TreeExcelUtilTest {

    @Test
    public void getTrees(){
//        String path = "C:\\Users\\chenm\\Desktop\\公司重要文档\\题库逻辑模板.xlsx";
        String path = "C:\\Users\\chenm\\Desktop\\公司重要文档\\逻辑搭建模板 -首问可多选-随机.xlsx";
        PoiExcelUtil.ReadUtil.PoiExcelResult poiExcelResult = PoiExcelUtil.ReadUtil.readExcel(path);
        List<List<String>> lists = poiExcelResult.getData();
        lists.forEach(System.out::println);

        List<TreeExcelUtil.TreeNode> treeNodes = TreeExcelUtil.getTreeNodes(lists);

        treeNodes.forEach(System.out::println);


        List<TreeExcelUtil.TreeNode> trees = TreeExcelUtil.getTrees(lists);
        trees.forEach(tree ->{
            System.out.println(JSON.toJSONString(tree));
        });
    }
}
