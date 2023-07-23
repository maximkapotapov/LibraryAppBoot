package com.github.library_app_boot.controllers;


import com.github.library_app_boot.models.Book;
import com.github.library_app_boot.models.Person;
import com.github.library_app_boot.services.BooksService;
import com.github.library_app_boot.services.PeopleService;
import com.github.library_app_boot.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookValidator bookValidator;

    private final BooksService booksService;

    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookValidator bookValidator, BooksService booksService, PeopleService peopleService) {
        this.bookValidator = bookValidator;
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showAll(Model model, @RequestParam(value = "page") Optional<Integer> page,
                          @RequestParam(value = "books_per_page") Optional<Integer> booksPerPage,
                          @RequestParam(value = "sort_by_year") Optional<String> sortByYear) {

        if(page.isPresent() && booksPerPage.isPresent())
            model.addAttribute("books", booksService.booksPerPage(page.get(), booksPerPage.get()));
        if(page.isEmpty() && booksPerPage.isEmpty())
            model.addAttribute("books", booksService.findAll());
        if(sortByYear.isPresent() && sortByYear.get().equals("true"))
            model.addAttribute("books", booksService.sortByYear());
        if(page.isPresent() && booksPerPage.isPresent() && sortByYear.isPresent() && sortByYear.get().equals("true"))
            model.addAttribute("books", booksService.booksPerPageSorted(page.get(), booksPerPage.get()));

        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if(bindingResult.hasErrors())
            return "books/new";

        booksService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        bookValidator.validate(book, bindingResult);
        if(bindingResult.hasErrors())
            return "books/edit";

        booksService.updateBook(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(Model model, @PathVariable("id") int id, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(id));
        model.addAttribute("personId", booksService.findOwnerByBookId(id));
        model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @PatchMapping("/{id}/edit")
    public String deletePersonBook(@PathVariable("id") int id){
        booksService.deletePersonBook(id);
        return "redirect:/books/{id}";
    }

    @PostMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.updatePersonBook(id, person);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(Model model, @Param("keyword") String keyword) {
        Book book = booksService.findBookByTitle(keyword);

        model.addAttribute("book", book);
        model.addAttribute("keyword", keyword);
        model.addAttribute("owner", booksService.findOwnerOfBook(book));
        return "books/search";
    }

}
