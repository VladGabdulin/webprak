package com.cmc.sp.webprak.controllers;

import com.cmc.sp.webprak.DAO.OperationDetailsDAO;
import com.cmc.sp.webprak.DAO.PartnersDAO;
import com.cmc.sp.webprak.DAO.ProductsDAO;
import com.cmc.sp.webprak.classes.OperationDetails;
import com.cmc.sp.webprak.classes.Partners;
import com.cmc.sp.webprak.classes.Products;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    private final ProductsDAO productsDAO;
    private final OperationDetailsDAO operationDetailsDAO;
    private final PartnersDAO partnersDAO;

    public MainController(ProductsDAO productsDAO, OperationDetailsDAO operationDetailsDAO) {
        this.productsDAO = productsDAO;
        this.operationDetailsDAO = operationDetailsDAO;
        this.partnersDAO = new PartnersDAO();
    }

    @RequestMapping(value = { "/", "/main_page"})
    public String main_page() {
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
        return "product-form";
    }

    @GetMapping("/products/edit")
    public String showEditFormProduct(@RequestParam("productId") Long productId, Model model) {
        Products product = productId != 0 ? productsDAO.getById(productId) : new Products();
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

    // Страница редактирования товара
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
}