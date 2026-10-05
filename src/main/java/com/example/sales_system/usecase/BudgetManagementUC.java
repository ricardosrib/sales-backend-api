package com.example.sales_system.usecase;

import com.example.sales_system.domain.model.BudgetModel;
import com.example.sales_system.domain.services.SalesService;
import com.example.sales_system.exception.BadRequestException;
import com.example.sales_system.usecase.dto.BudgetDTO;
import com.example.sales_system.usecase.dto.BudgetPageDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class BudgetManagementUC {
    private final SalesService salesService;

    public BudgetManagementUC(SalesService salesService) { this.salesService = salesService; }

    public BudgetPageDTO list(Long customerId, Boolean finalized, LocalDate from, LocalDate to, int page, int size) {
        validatePaging(page, size);
        List<BudgetModel> matches = salesService.listBudgets(customerId, finalized, from, to);
        return page(matches, page, size);
    }

    public BudgetPageDTO customerBudgets(long customerId, int page, int size) {
        validatePaging(page, size);
        return page(salesService.listCustomerBudgets(customerId), page, size);
    }

    public void delete(long id) { salesService.deleteBudget(id); }
    public BudgetDTO cancel(long id) { return BudgetDTO.fromModel(salesService.cancelBudget(id)); }
    public BudgetDTO duplicate(long id) { return BudgetDTO.fromModel(salesService.duplicateBudget(id)); }
    public BudgetDTO addOrUpdateItem(long id, long productId, int quantity) {
        return BudgetDTO.fromModel(salesService.addOrUpdateBudgetItem(id, productId, quantity));
    }

    private BudgetPageDTO page(List<BudgetModel> matches, int page, int size) {
        long offset = (long) page * size;
        List<BudgetModel> content = offset >= matches.size() ? List.of()
                : matches.subList((int) offset, Math.min(matches.size(), (int) offset + size));
        return new BudgetPageDTO(matches, content, page, size);
    }

    private void validatePaging(int page, int size) {
        if (page < 0 || size < 1 || size > 100) throw new BadRequestException("page must be >= 0 and size must be between 1 and 100");
    }

    private BudgetPageDTO page(List<BudgetModel> all, List<BudgetModel> content, int page, int size) {
        return new BudgetPageDTO(content, page, size, all.size());
    }
}
