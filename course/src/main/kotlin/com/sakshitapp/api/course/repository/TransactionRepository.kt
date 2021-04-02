package com.sakshitapp.api.course.repository

import com.sakshitapp.api.base.model.Transaction
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : ReactiveMongoRepository<Transaction, String>