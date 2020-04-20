package com.example.sufya.draft.recycler_Views.expandable;

import com.example.sufya.draft.recycler_View_Models.expandable_section;

/**
 * @Author bpncool. (2016). .
 * @website https://github.com/bpncool/SectionedExpandableGridRecyclerView/tree/master/app/src/main/res/layout
 *
 */

public interface SectionStateChangeListener {
    void onSectionStateChanged(expandable_section section, boolean isOpen);
}
