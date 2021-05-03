package com.sakshitapp.api.base.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.sakshitapp.api.base.model.Notification
import com.sakshitapp.api.base.model.NotificationType
import com.sakshitapp.api.base.model.RedirectType
import com.sakshitapp.api.base.model.User
import com.sakshitapp.api.base.repository.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NotificationService {

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @Autowired
    private lateinit var firebaseMessaging: FirebaseMessaging

    fun sendNotification(title: String?, content: String?, image: String?, redirects: String?, redirectType: RedirectType?, user: User?): Mono<Notification> {
        var notification = Notification(title = title, content = content, image = image, redirects = redirects, redirectType = redirectType, typeId = user?.uid, type = if (user?.uid == null) NotificationType.ALL else NotificationType.USER)
        val messaging = if (user == null) {
            Message.builder()
                    .setTopic("SakshitApp")
        } else {
            Message.builder()
                    .setToken(user?.fcmToken)
        }
        val string = ObjectMapper()
                .writeValueAsString(notification)
        messaging.putData("item", string)
        notification = notification.copy(
                messageId = firebaseMessaging.send(messaging.build())
        )
        return notificationRepository.save(notification)
    }

    fun getNotification(user: User): Mono<List<Notification>> =
            notificationRepository.findByUser(user.uid)
                    .collectList()
}