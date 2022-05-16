package by.mironovich.application.crudapplication.controllers;

import by.mironovich.application.crudapplication.dao.PersonDAO;
import by.mironovich.application.crudapplication.models.Person;
import by.mironovich.application.crudapplication.models.exceptions.PersonNotFoundException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private PersonDAO personDAO;

    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) throws PersonNotFoundException {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String createPerson(Model model) {
        model.addAttribute(new Person());
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("age")) {
                checkAgeErrorsOnTypeMismatch(person, bindingResult, model);
            }
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id) throws PersonNotFoundException {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable int id) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        personDAO.update(id, person);
        return "people/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

    private void checkAgeErrorsOnTypeMismatch(Person person, BindingResult bindingResult, Model model) {
        FieldError ageError = bindingResult.getFieldError("age");
        if (ageError != null && ageError.getCodes() != null) {
            for (String code : ageError.getCodes()) {
                if (code.contains("typeMismatch")) {
                    replaceMismatchError(person, bindingResult, model, ageError);
                    break;
                }
            }
        }
    }

    private void replaceMismatchError(Person person, BindingResult bindingResult, Model model, FieldError ageError) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream().filter(fieldError -> !fieldError.getField().equals("age")).collect(Collectors.toList());
        FieldError objectError = new FieldError("person", "age", ageError.getRejectedValue(), ageError.isBindingFailure(), null, null, "Not String!!!");
        errorsToKeep.add(objectError);
        bindingResult = new BeanPropertyBindingResult(person, "person");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }
        model.addAttribute("org.springframework.validation.BindingResult.person", bindingResult);
    }
}
