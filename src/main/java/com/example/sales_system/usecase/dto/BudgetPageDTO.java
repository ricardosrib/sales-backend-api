package com.example.sales_system.usecase.dto;

import com.example.sales_system.domain.model.BudgetModel;
import java.util.List;

public class BudgetPageDTO {
    private final List<BudgetDTO> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    public BudgetPageDTO(List<BudgetModel> budgets, int page, int size) {
        this(budgets, page, size, budgets.size());
    }

    public BudgetPageDTO(List<BudgetModel> budgets, List<BudgetModel> content, int page, int size) {
        this(content, page, size, budgets.size());
    }

    public BudgetPageDTO(List<BudgetModel> budgets, int page, int size, long totalElements) {
        this.content = budgets.stream().map(BudgetDTO::fromModel).toList();
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
    }

    public List<BudgetDTO> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
}
