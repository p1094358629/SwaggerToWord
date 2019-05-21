package com.word.controller;

import com.word.dto.DefinitionsBean;
import com.word.dto.Table;
import com.word.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author xuym26145
 * @Description
 * @Date 2019/5/21 14:22
 * @Param 
 * @return 
 **/
@Controller
public class WordController {

    @Autowired
    private WordService tableService;

    @RequestMapping("/getWord")
    public String getJson(Model model){
        transform(model);
        return "word";
    }
    @RequestMapping("/getHtml")
    public String getHtml(Model model){
        transform(model);
        return "html";
    }
    private void transform(Model model) {
        model.addAttribute("table", getTables());
        model.addAttribute("definitions", definitionsBean());
    }
    private List<Table> getTables() {
        return tableService.tableList();
    }
    private List<DefinitionsBean> definitionsBean(){
        return tableService.definitionsBean();
    }

}
