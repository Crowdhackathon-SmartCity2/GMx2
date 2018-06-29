package gr.smartcity.hackathon.hackathonproject;

/**
 * This class illustrates a sample interface for sending data through GUI forms.
 */
public interface ItemSelectionListener<T> {
    void onItemSelected(T item);
}