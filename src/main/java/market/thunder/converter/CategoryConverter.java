package market.thunder.converter;

import market.thunder.domain.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

public class CategoryConverter implements Converter<String, Category> {
    @Override
    public Category convert(String strCategory){
        return Category.getCategory(strCategory);
    }
}
