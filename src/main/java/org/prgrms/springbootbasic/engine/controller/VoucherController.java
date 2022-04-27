package org.prgrms.springbootbasic.engine.controller;

import org.prgrms.springbootbasic.engine.controller.dto.CustomerResponseDto;
import org.prgrms.springbootbasic.engine.controller.dto.VoucherCreateRequestDto;
import org.prgrms.springbootbasic.engine.controller.dto.VoucherResponseDto;
import org.prgrms.springbootbasic.engine.domain.Customer;
import org.prgrms.springbootbasic.engine.domain.Voucher;
import org.prgrms.springbootbasic.engine.service.CustomerService;
import org.prgrms.springbootbasic.engine.service.VoucherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.prgrms.springbootbasic.engine.util.UUIDUtil.convertStringToUUID;

@Controller
public class VoucherController {
    private final VoucherService voucherService;

    private final CustomerService customerService;

    public VoucherController(VoucherService voucherService, CustomerService customerService) {
        this.voucherService = voucherService;
        this.customerService = customerService;
    }

    @GetMapping("/vouchers")
    public String viewVouchersPage(Model model) {
        List<VoucherResponseDto> allVouchers = voucherService
                .getAllVouchers()
                .stream()
                .map(VoucherResponseDto::new)
                .collect(Collectors.toList());
        model.addAttribute("vouchers", allVouchers);
        return "views/vouchers";
    }

    @GetMapping("/vouchers/new")
    public String viewNewVoucherPage(Model model) {
        List<CustomerResponseDto> customers = customerService.getAllCustomers().stream().map(CustomerResponseDto::new).toList();
        model.addAttribute("customers", customers);
        return "views/new-voucher";
    }

    @PostMapping("/vouchers/new")
    public String createNewVoucher(VoucherCreateRequestDto voucherCreateRequestDto) {
        Voucher voucher = voucherCreateRequestDto.toEntity();
        if (voucherCreateRequestDto.getCustomerId().isPresent()) {
            Customer customer = customerService.getCustomerById(voucherCreateRequestDto.getCustomerId().get());
            voucher.changeOwner(customer);
        }
        voucherService.insertVoucher(voucher);
        return "redirect:/vouchers/" + voucher.getVoucherId();
    }

    @GetMapping("/vouchers/{voucherId}")
    public String viewVoucherDetailPage(Model model, @PathVariable String voucherId) {
        UUID id = convertStringToUUID(voucherId);
        Voucher voucher = voucherService.getVoucher(id);
        model.addAttribute("voucher", new VoucherResponseDto(voucher));
        return "views/voucher";
    }

    @GetMapping("/vouchers/{voucherId}/delete")
    public String deleteVoucher(@PathVariable String voucherId) {
        UUID id = convertStringToUUID(voucherId);
        voucherService.removeVoucherById(id);
        return "redirect:/vouchers";
    }
}
