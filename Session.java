// ArrayList is the resizable list we use to store the dataset numbers
import java.util.ArrayList;
// List is the general type used as the variable type for our dataset
import java.util.List;

// Session stores information about ONE connected client — their dataset and whether it's sorted
// Every client gets their own Session object so their data stays separate from other clients
public class Session {
    // dataset holds the list of numbers the client has loaded
    // It starts as null (nothing loaded yet)
    private List<Integer> dataset;

    // sorted tracks whether the current dataset has been sorted
    // This matters because binary search only works on sorted data
    private boolean sorted;

    // Constructor — called whenever a new client connects and we create a fresh Session
    public Session() {
        this.dataset = null;   // no data loaded yet
        this.sorted = false;   // not sorted (nothing to sort anyway)
    }

    // setDataset stores a new list of numbers into this session
    // We make a copy (new ArrayList) so changes outside won't accidentally affect our stored data
    public void setDataset(List<Integer> dataset) {
        this.dataset = new ArrayList<>(dataset); // copy the list rather than sharing the reference
        this.sorted = false; // a new dataset is considered unsorted until explicitly sorted
    }

    // getDataset returns the list of numbers currently stored in this session
    public List<Integer> getDataset() {
        return dataset;
    }

    // hasDataset returns true if the client has already loaded at least one number
    // It returns false if dataset is null OR if the list is empty
    public boolean hasDataset() {
        return dataset != null && !dataset.isEmpty();
    }

    // isSorted returns true if the dataset has been sorted since it was last loaded
    public boolean isSorted() {
        return sorted;
    }

    // setSorted lets the server mark the dataset as sorted (or unsorted)
    // Called with "true" after a sort command succeeds
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    // clear wipes out all data in the session — like pressing a reset button
    // After this, hasDataset() will return false and the client must load new data
    public void clear() {
        this.dataset = null;   // remove the stored numbers
        this.sorted = false;   // reset the sorted flag too
    }
}
