package com.djavid.smartsubs.utils

enum class Event(val value: String) {
    //notif
    ACTIVATE_NOTIF_CLICKED("activate_notif_clicked"),
    EDIT_NOTIF_CLICKED("edit_notif_clicked"),
    ADD_NOTIF_CLICKED("add_notif_clicked"),
    SHOW_NOTIFS_CLICKED("show_notifs_clicked"),

    NOTIF_CREATED("notif_created"),
    NOTIF_EDITED("notif_edited"),
    NOTIF_CHECK_CHANGED("notif_check_changed"),

    //sub
    EDIT_SUB_CLICKED("edit_sub_clicked"),
    ADD_SUB_CLICKED("add_sub_clicked"),

    SUB_EDITED("sub_edited"),
    SUB_CREATED("sub_created"),
    SUB_ITEM_CLICKED("sub_item_clicked"),
    SUB_SWIPED_LEFT("sub_swiped_left"),
    SUB_DELETED("sub_deleted"),

    //sort
    SORT_BY_CHANGED("sort_by_changed"),
    SORT_TYPE_CHANGED("sort_type_changed"),
    SORT_BTN_CLICKED("sort_btn_clicked"),

    //home
    PERIOD_CHANGE_CLICKED("period_changed_clicked"),
}