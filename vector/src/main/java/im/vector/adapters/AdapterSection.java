/*
 * Copyright 2017 Vector Creations Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.adapters;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterSection<T> {
    private final String mTitle;

    private String mTitleFormatted;
    // Place holder if no item for the section
    private String mNoItemPlaceholder;
    // Place holder if no result after search
    private String mNoResultPlaceholder;

    private int mHeaderSubView;
    private int mContentView;
    private int mHeaderViewType;
    private int mContentViewType;

    private List<T> mItems;

    private List<T> mFilteredItems;

    private Comparator<T> mComparator;

    private CharSequence mCurrentFilterPattern;

    private boolean mIsHiddenWhenEmpty;

    public AdapterSection(String title, int headerSubViewResId, int contentResId, int headerViewType,
                          int contentViewType, List<T> items, Comparator<T> comparator) {
        mTitle = title;
        mItems = items;
        mFilteredItems = new ArrayList<>(items);
        mHeaderSubView = headerSubViewResId;
        mContentView = contentResId;

        mHeaderViewType = headerViewType;
        mContentViewType = contentViewType;
        mComparator = comparator;

        updateTitle();
    }

    /**
     * Update items list
     *
     * @param items
     * @param currentFilterPattern
     */
    public void setItems(List<T> items, CharSequence currentFilterPattern) {
        if (mComparator != null) {
            Collections.sort(items, mComparator);
        }
        mItems.clear();
        mItems.addAll(items);

        setFilteredItems(items, currentFilterPattern);
    }

    /**
     * Update the filtered list of items using the given items and pattern
     *
     * @param items
     * @param currentFilterPattern
     */
    public void setFilteredItems(List<T> items, CharSequence currentFilterPattern) {
        mFilteredItems.clear();
        mFilteredItems.addAll(items);

        mCurrentFilterPattern = currentFilterPattern;
        updateTitle();
    }

    /**
     * Update the title depending on the number of items
     */
    private void updateTitle() {
        if (getNbItems() > 0) {
            mTitleFormatted = mTitle.concat(" (" + getNbItems() + ")");
        } else {
            mTitleFormatted = mTitle;
        }
    }

    /**
     * Get title
     *
     * @return
     */
    public String getTitle() {
        return mTitleFormatted;
    }

    /**
     * Get the layout resId of the custom view that should be added to the header
     *
     * @return
     */
    public int getHeaderSubView() {
        return mHeaderSubView;
    }

    /**
     * Get the text to display when there is no item for the section
     *
     * @return
     */
    public String getEmptyViewPlaceholder() {
        return TextUtils.isEmpty(mCurrentFilterPattern) ? mNoItemPlaceholder : mNoResultPlaceholder;
    }

    /**
     * Set the text to display when there is no item/no result
     *
     * @param noItemPlaceholder
     */
    public void setEmptyViewPlaceholder(final String noItemPlaceholder) {
        mNoItemPlaceholder = noItemPlaceholder;
        mNoResultPlaceholder = noItemPlaceholder;
    }

    /**
     * Set the texts to display when there is no item or no result
     *
     * @param noItemPlaceholder
     */
    public void setEmptyViewPlaceholder(final String noItemPlaceholder, final String noResultPlaceholder) {
        mNoItemPlaceholder = noItemPlaceholder;
        mNoResultPlaceholder = noResultPlaceholder;
    }

    /**
     * Get the header view type for the header (used by adapter)
     *
     * @return
     */
    public int getHeaderViewType() {
        return mHeaderViewType;
    }

    /**
     * Get the content view type for the header (used by adapter)
     *
     * @return
     */
    public int getContentViewType() {
        return mContentViewType;
    }

    /**
     * Get the list of items
     *
     * @return
     */
    public List<T> getItems() {
        return mItems;
    }

    /**
     * Get the list of items matching the current filter
     *
     * @return
     */
    public List<T> getFilteredItems() {
        return mFilteredItems;
    }

    /**
     * Get the number of items matching the current filter
     *
     * @return
     */
    public int getNbItems() {
        return mFilteredItems.size();
    }

    /**
     * Update the filtered list by removing the current filter
     */
    public void resetFilter() {
        mFilteredItems.clear();
        mFilteredItems.addAll(mItems);

        mCurrentFilterPattern = null;
        updateTitle();
    }

    /**
     * Set whether the section should be hidden when it has no item
     *
     * @return
     */
    public void setIsHiddenWhenEmpty(final boolean isHiddenWhenEmpty) {
        mIsHiddenWhenEmpty = isHiddenWhenEmpty;
    }

    /**
     * Get whether the section should be hidden when it has no item
     *
     * @return
     */
    public boolean hideWhenEmpty() {
        return mIsHiddenWhenEmpty;
    }

    /**
     * Remove an item from the section
     *
     * @param object
     * @return
     */
    public boolean removeItem(final T object) {
        if (mFilteredItems.contains(object)) {
            mFilteredItems.remove(object);
            updateTitle();
            return true;
        }
        mItems.remove(object);
        return false;
    }
}
