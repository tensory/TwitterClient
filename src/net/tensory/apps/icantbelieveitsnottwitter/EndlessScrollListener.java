package net.tensory.apps.icantbelieveitsnottwitter;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class EndlessScrollListener implements OnScrollListener {
	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 5;
	// The current offset index of data you have loaded
	private int currentPage = 0;
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	// True if we are still waiting for the last set of data to load.
	private boolean loading = true;
	// Sets the starting page index
	private int startingPageIndex = 0;

	public EndlessScrollListener() {
		// TODO Auto-generated constructor stub
	}
	
	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}
	
	public EndlessScrollListener(int visibleThreshold, int startPage) {
		this.visibleThreshold = visibleThreshold;
		this.startingPageIndex = startPage;
		this.currentPage = startPage;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// If the total item count is zero and the previous isn't, assume the
		// list is invalidated and should be reset back to initial state
		if (!this.loading && (totalItemCount < previousTotalItemCount)) {
			this.currentPage = this.startingPageIndex;
			this.previousTotalItemCount = totalItemCount;
			this.loading = true;
		}
		
		// If it's still loading, we check to see if the dataset count has
		// changed, if so we conclude it has finished loading and update the current page
		// number and total item count.
		if (this.loading) {
			if (totalItemCount > previousTotalItemCount) {
				this.loading = false;
				this.previousTotalItemCount = totalItemCount;
				currentPage++;
			}
		}
		
		// If it isn't currently loading, we check to see if we have breached
		// the visibleThreshold and need to reload more data.
		// If we do need to reload some more data, we execute loadMore to fetch the data.
		if (!this.loading && (totalItemCount - visibleItemCount) <= firstVisibleItem + visibleThreshold) {
			loadMore(currentPage + 1, totalItemCount);
			this.loading = true;
		}
	}
	
	// Defines process for actually loading more data
	public abstract void loadMore(int page, int totalItemsCount);

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
