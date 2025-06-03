package com.cmc.sp.webprak.controllers;

import com.cmc.sp.webprak.DAO.*;
import com.cmc.sp.webprak.classes.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    private final ProductsDAO productsDAO;
    private final OperationDetailsDAO operationDetailsDAO;
    private final PartnersDAO partnersDAO;
    private final OperationsDAO operationsDAO;
    private final UsersDAO usersDAO;

    public MainController(ProductsDAO productsDAO, OperationDetailsDAO operationDetailsDAO, PartnersDAO partnersDAO, OperationsDAO operationsDAO, UsersDAO usersDAO) {
        this.productsDAO = productsDAO;
        this.operationDetailsDAO = operationDetailsDAO;
        this.partnersDAO = partnersDAO;
        this.operationsDAO = operationsDAO;
        this.usersDAO = usersDAO;
    }

    @RequestMapping(value = { "/", "/main_page"})
    public String main_page(Model model) {
        long partnersCount = partnersDAO.getCount(Partners.class);
        long productsCount = productsDAO.getCount(Products.class);
        long operationsCount = operationsDAO.getCount(Operations.class);

        model.addAttribute("partnersCount", partnersCount);
        model.addAttribute("productsCount", productsCount);
        model.addAttribute("operationsCount", operationsCount);
        return "main_page";
    }

    @RequestMapping(value = "/allProducts" )
    public String allProducts() {
        return "products";
    }

    @GetMapping("/products")
    public String productsListPage(Model model) {
        List<Products> products = (List<Products>) productsDAO.getAll();
        model.addAttribute("products", products);
        model.addAttribute("productsService", productsDAO);
        return "products";
    }

    @GetMapping("/product")
    public String getProductDetails(@RequestParam("productId") Long productId, Model model) {
        Products product = productsDAO.getById(productId);
        List<OperationDetails> operationDetails = operationDetailsDAO.getOperationsDetailsByProduct(product);

        if (product == null) {
            return "products";
        }

        model.addAttribute("product", product);
        model.addAttribute("productsService", productsDAO);
        model.addAttribute("operationDetails", operationDetails);
        model.addAttribute("operationDetailsService", operationDetailsDAO);
        return "product_details";
    }

    @GetMapping("/add_product")
    public String showAddFormProduct(Model model) {
        model.addAttribute("product", new Products());
        model.addAttribute("isEdit", false);
        return "product_form";
    }

    @GetMapping("/products/edit")
    public String showEditFormProduct(@RequestParam("productId") Long productId, Model model) {
        Products product = productsDAO.getById(productId);
        model.addAttribute("product", product);
        model.addAttribute("isEdit", true);
        return "product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Products product) {
        productsDAO.save(product);
        return "redirect:/products";
    }

    @GetMapping("/partners")
    public String partnersListPage(Model model) {
        List<Partners> partners = (List<Partners>) partnersDAO.getAll();
        model.addAttribute("partners", partners);
        model.addAttribute("partnersService", partnersDAO);
        return "partners";
    }

    @GetMapping("/partner")
    public String getPartnerDetails(@RequestParam("partnerId") Long partnerId, Model model) {
        Partners partner = partnersDAO.getById(partnerId);
        model.addAttribute("partner", partner);
        model.addAttribute("partnersService", partnersDAO);
        return "partner_details";
    }

    @GetMapping("/add_partner")
    public String showAddFormPartner(Model model) {
        model.addAttribute("partner", new Partners());
        model.addAttribute("isEdit", false);
        return "partner_form";
    }

    @GetMapping("/partners/edit")
    public String showEditFormPartner(@RequestParam("partnerId") Long partnerId, Model model) {
        Partners partner = partnerId != 0 ? partnersDAO.getById(partnerId) : new Partners();
        model.addAttribute("partner", partner);
        model.addAttribute("isEdit", true);
        return "partner_form";
    }

    @PostMapping("/partners/save")
    public String savePartner(@ModelAttribute Partners partner) {
        partnersDAO.save(partner);
        return "redirect:/partners";
    }

    @ModelAttribute("partnerTypes")
    public Partners.PartnerType[] getPartnerTypes() {
        return Partners.PartnerType.values();
    }

    @GetMapping("/operations")
    public String showOperations(Model model) {
        List<Operations> operations = (List<Operations>) operationsDAO.getAll();
        model.addAttribute("operations", operations);
        model.addAttribute("operationsService", operationsDAO);
        model.addAttribute("operationDetailsService", operationDetailsDAO);
        return "operations";
    }

    @GetMapping("/add_operation")
    public String showAddFormOperation(Model model) {
        model.addAttribute("operation", new Operations());
        model.addAttribute("isEdit", false);
        List<Partners> partners = (List<Partners>) partnersDAO.getAll();
        model.addAttribute("partners", partners);
        model.addAttribute("partnersService", partnersDAO);
        return "operation_form";
    }

    @GetMapping("/operations/edit")
    public String showEditFormOperation(@RequestParam("operationId") Long operationId, Model model) {
        Operations operation = operationId != 0 ? operationsDAO.getById(operationId) : new Operations();
        model.addAttribute("operation", operation);
        model.addAttribute("isEdit", true);
        List<Partners> partners = (List<Partners>) partnersDAO.getAll();
        model.addAttribute("partners", partners);
        model.addAttribute("partnersService", partnersDAO);
        return "operation_form";
    }

    @ModelAttribute("operationTypes")
    public Operations.OperationType[] getOperationTypes() {
        return Operations.OperationType.values();
    }

    @PostMapping("/operations/save")
    public String saveOperation(@ModelAttribute Operations operation, @RequestParam Long partnerId) {
        Partners partner = partnersDAO.getById(partnerId);
        operation.setPartner(partner);
        operation.setUser(usersDAO.getById(1L));
        operationsDAO.save(operation);
        return "redirect:/operations";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("operationsCount", operationsDAO.getCount(Operations.class));
        model.addAttribute("productsCount", productsDAO.getCount(Products.class));
        model.addAttribute("partnersCount", partnersDAO.getCount(Partners.class));
        return "main_page";
    }
}