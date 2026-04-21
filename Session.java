import java.util.ArrayList;
import java.util.List;

public class Session {
    private List<Integer> dataset;
    private boolean sorted;

    public Session() {
        this.dataset = null;
        this.sorted = false;
    }

    public void setDataset(List<Integer> dataset) {
        this.dataset = new ArrayList<>(dataset);
        this.sorted = false;
    }

    public List<Integer> getDataset() {
        return dataset;
    }

    public boolean hasDataset() {
        return dataset != null && !dataset.isEmpty();
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void clear() {
        this.dataset = null;
        this.sorted = false;
    }
}
