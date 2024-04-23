package bean.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

@Controller
public class WebController {
    @Autowired
    MyBankService service;
    @GetMapping("/")
    public String landing(Model model){
        CreditCard creditCard=new CreditCard();
        model.addAttribute("creditCard",creditCard);
        return "index";
    }
    @PostMapping("/")
    public String submission(@Valid CreditCard creditCard, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("creditCard",creditCard);
        }
        else {
            //System.out.println(creditCard.toString());
            service.apiSave(creditCard);
            model.addAttribute("message", "Card Approved");
        }
        return "index";
    }

    @GetMapping("/dash")
    public String dashboard(Model model){
        List<CreditCard> all;
        try {
            all = service.apiFindAll();
        } catch (SQLSyntaxErrorException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("all",all);
        return "dashboard";
    }

    @GetMapping("/one/{number}")
    public String readOne(@PathVariable("number") Long number, Model model){
        CreditCard creditCard = service.apiFindById(number).get();
        model.addAttribute("creditCard",creditCard);
        return "update";
    }

    @PostMapping("/up")
    public String submitEdit(CreditCard creditCard, Model model){
        service.apiUpdate(creditCard,100);
        return "redirect:/dash";
    }

    @GetMapping("/del/{number}/{pin}")
    public String deleteOne(@PathVariable("number") Long number,@PathVariable("pin") Integer pin, Model model){
        service.apiBlock(number,pin);
        return "redirect:/dash";
    }
}
