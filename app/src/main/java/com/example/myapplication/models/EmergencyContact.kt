
package com.example.myapplication.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class EmergencyContact(
    var id: String? = null, // Firebase Realtime Database key
    var name: String? = null,
    var phoneNumber: String? = null
) {
    // No-argument constructor for Firebase deserialization
    constructor() : this(null, null, null)
}
