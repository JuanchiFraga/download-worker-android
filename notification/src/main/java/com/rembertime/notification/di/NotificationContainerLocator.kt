package com.rembertime.notification.di

import android.content.Context

internal object NotificationContainerLocator {

    private var notificationContainer: NotificationContainer? = null

    /**
     * Returns the currently set NotificationContainer. If there is non, creates and returns a NotificationContainer
     *
     * @return The currently set NotificationContainer
     */
    fun locateComponent(context: Context): NotificationContainer {
        if (notificationContainer == null) {
            setComponent(NotificationContainerDefault(context.applicationContext))
        }
        return notificationContainer!!
    }

    /**
     * Replaces the NotificationContainer with the given one
     *
     * @param notificationContainer The NotificationContainer to set
     */
    fun setComponent(notificationContainer: NotificationContainer) {
        NotificationContainerLocator.notificationContainer = notificationContainer
    }
}