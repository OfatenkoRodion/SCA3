package util

import java.util.Date

import org.apache.commons.lang3.time.DateUtils

object JwtImpl {

  def getToken(login: String, password: String): String = {
    import com.auth0.jwt.JWT
    import com.auth0.jwt.algorithms.Algorithm

  JWT.create.withIssuer(login).withExpiresAt(DateUtils.addMinutes(new Date(), 1)).sign(Algorithm.HMAC256(password))

  }

}
