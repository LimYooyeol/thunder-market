package market.thunder.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PagingInfo {

    private int current;
    private int last;

    public PagingInfo(int current, int last){
        this.current = current;
        this.last = last;
    }
}
