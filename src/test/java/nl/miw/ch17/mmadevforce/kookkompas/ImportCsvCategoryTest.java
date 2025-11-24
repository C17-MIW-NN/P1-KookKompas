package nl.miw.ch17.mmadevforce.kookkompas;

import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.service.CategoryService;
import nl.miw.ch17.mmadevforce.kookkompas.service.InitializeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Arjen Zijlstra
 *
 */

@ExtendWith(MockitoExtension.class)
public class ImportCsvCategoryTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private InitializeService testInitialService;

    @Test @DisplayName("Testimport for categories via CSV when category exists")
    void testImportExistingCategory() {

        //Arrange
        String line = "ontbijt,#FFFFFF";
        Category existingCategory = new Category();

        existingCategory.setCategoryName("ontbijt");
        existingCategory.setCategoryColor("#000000");

        when(categoryService.findByCategoryName("ontbijt"))
                .thenReturn(Optional.of(existingCategory));

        //Act
        Category result = testInitialService.importCSVFileCategoryList(line);

        //Assert
        assertEquals("ontbijt", result.getCategoryName());
        assertEquals("#FFFFFF", result.getCategoryColor());
        verify(categoryService).saveCategory(existingCategory);
    }

    @Test @DisplayName("Testimport for categories via CSV when category does not exist")
    void testImportNewCategory() {

        //Arrange
        String line = "Lunch,#228b22";

        when(categoryService.findByCategoryName("Lunch"))
                .thenReturn(Optional.empty());

        //Act
        Category result = testInitialService.importCSVFileCategoryList(line);

        //Assert
        assertEquals("Lunch", result.getCategoryName());
        assertNotNull(result.getCategoryColor());
        assertFalse(result.getCategoryColor().isBlank());
        verify(categoryService).saveCategory(result);
    }
}
