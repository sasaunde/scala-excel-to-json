package core

import java.util.UUID
import api.DefaultJsonFormats
import spray.http.Uri

/**
 * A "typical" user value containing its identity, name and email.
 *
 * @param id the identity
 * @param firstName the first name
 * @param lastName the last name
 * @param email the email address
 */
case class User(id: String, firstName: String, lastName: String, email: String, baseLoc: String, photoUrl: Uri)
