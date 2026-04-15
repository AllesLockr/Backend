package eu.luftiger.cae.web.common.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.substring(7)

        try {
            jwtService.validate(token)
            val userId = jwtService.extractUserId(token)

            val auth = UsernamePasswordAuthenticationToken(
                userId,
                null,
                emptyList()
            )

            val context = SecurityContextHolder.createEmptyContext()
            context.authentication = auth
            SecurityContextHolder.setContext(context)

        } catch (_: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("""{"message":"Invalid or expired token"}""")
            return
        }

        filterChain.doFilter(request, response)
    }
}