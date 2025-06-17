package com.example.presentation.ui.home.usecase

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import com.example.presentation.entities.MusicListItem
import javax.inject.Inject


class InsertSeparatorIntoPagingData @Inject constructor() {
    fun insert(pagingData: PagingData<MusicListItem.Music>): PagingData<MusicListItem> {
        return pagingData.insertSeparators { before: MusicListItem.Music?, after: MusicListItem.Music? ->
            when {
                isListEmpty(before, after) -> null
                isHeader(before) -> insertHeaderItem(after)
                isFooter(after) -> insertFooterItem()
                isDifferentCategory(before, after) -> insertSeparatorItem(after)
                else -> null
            }
        }
    }

    private fun isListEmpty(before: MusicListItem.Music?, after: MusicListItem.Music?): Boolean = before == null && after == null

    private fun isHeader(before: MusicListItem.Music?): Boolean = before == null

    private fun isFooter(after: MusicListItem.Music?): Boolean = after == null

    private fun isDifferentCategory(before: MusicListItem.Music?, after: MusicListItem.Music?): Boolean =
        before?.category != after?.category

    /**
     * Insert Header; return null to skip adding a header.
     * **/
    private fun insertHeaderItem(after: MusicListItem.Music?): MusicListItem? = createSeparator(after)

    /**
     * Insert Footer; return null to skip adding a footer.
     * **/
    @Suppress("FunctionOnlyReturningConstant")
    private fun insertFooterItem(): MusicListItem? = null

    /**
     * Insert a separator between two items that start with different date.
     * **/
    private fun insertSeparatorItem(after: MusicListItem.Music?): MusicListItem.Separator? = createSeparator(after)

    private fun createSeparator(item: MusicListItem.Music?) = item?.let {
        MusicListItem.Separator(it.category)
    }
}
