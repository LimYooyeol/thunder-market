package market.thunder.domain;

public enum Category {
    TOYS,
    CLOTHES,
    ELECTRONICS;

    public static Category getCategory(String strCategory){
        for(Category category : Category.values()){
            if(category.toString().equals(strCategory)){
                return category;
            }
        }

        throw new IllegalStateException("No such category");
    }
}
