package com.clearance.app.controller;



import com.clearance.app.model.Log;
import com.clearance.app.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LogController {

    @Autowired
    LogRepository logRepository;

    @GetMapping("/logs")
    public String logs(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size)
    {

        Page<Log> logsPage;
        if(keyword !=null && !keyword.isEmpty())
        {

            logsPage = logRepository.findByKeyword(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")));
        }
        else
        {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
            logsPage = logRepository.findAll(pageable);
        }

        model.addAttribute("logs", logsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logsPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalItems", logsPage.getTotalElements());
        model.addAttribute("keyword", keyword);

        return "logs";
    }
}
