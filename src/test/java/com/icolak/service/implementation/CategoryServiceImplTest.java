package com.icolak.service.implementation;

import com.icolak.entity.Category;
import com.icolak.repository.CategoryRepository;
import com.icolak.testDocumentInitializer.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;


    @Test
    void delete_Test() {
        Category category = Category.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        categoryService.delete(category.getId());
        assertTrue(category.getIsDeleted());
    }
}