package modules

import repositories.MongoDBAuthInfoRepository
import services.UserService
import utils.MyEnv
import com.google.inject.name.Named
import com.google.inject.{ AbstractModule, Provides }
import com.mohiva.play.silhouette.api.crypto.{ Crypter, CrypterAuthenticatorEncoder }
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services._
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.api.{ Environment, EventBus, Silhouette, SilhouetteProvider }
import com.mohiva.play.silhouette.crypto.{ JcaCrypter, JcaCrypterSettings }
import com.mohiva.play.silhouette.impl.authenticators._
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.providers.oauth1.secrets.{ CookieSecretProvider, CookieSecretSettings }
import com.mohiva.play.silhouette.impl.services._
import com.mohiva.play.silhouette.impl.util._
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.persistence.daos.{ DelegableAuthInfoDAO, InMemoryAuthInfoDAO }
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.ceedubs.ficus.readers.EnumerationReader._
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.modules.reactivemongo.ReactiveMongoApi

/**
 * The Guice module which wires all Silhouette dependencies.
 */
class SilhouetteModule extends AbstractModule with ScalaModule {

  /**
   * Configures the module.
   */
  override def configure() {
    bind[Silhouette[MyEnv]].to[SilhouetteProvider[MyEnv]]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())

    // Replace this with the bindings to your concrete DAOs

    //   bind[DelegableAuthInfoDAO[PasswordInfo]].toInstance(MongoDBAuthInfoRepository[PasswordInfo])
    bind[DelegableAuthInfoDAO[OAuth1Info]].toInstance(new InMemoryAuthInfoDAO[OAuth1Info])
    bind[DelegableAuthInfoDAO[OAuth2Info]].toInstance(new InMemoryAuthInfoDAO[OAuth2Info])
    bind[DelegableAuthInfoDAO[OpenIDInfo]].toInstance(new InMemoryAuthInfoDAO[OpenIDInfo])
  }

  /**
   * Provides the implementation of the delegable password auth info DAO.
   *
   * @param reactiveMongoApi The ReactiveMongo API.
   * @return The implementation of the delegable password auth info DAO.
   */
  @Provides
  def providePasswordInfoDAO(reactiveMongoApi: ReactiveMongoApi): DelegableAuthInfoDAO[PasswordInfo] = {
    implicit lazy val format = Json.format[PasswordInfo]
    MongoDBAuthInfoRepository(reactiveMongoApi)
  }

  /**
   * Provides the HTTP layer implementation.
   *
   * @param client Play's WS client.
   * @return The HTTP layer implementation.
   */
  @Provides
  def provideHTTPLayer(client: WSClient): HTTPLayer = new PlayHTTPLayer(client)

  /**
   * Provides the Silhouette environment.
   *
   * @param userService The user service implementation.
   * @param authenticatorService The authentication service implementation.
   * @param eventBus The event bus instance.
   * @return The Silhouette environment.
   */
  @Provides
  def provideEnvironment(
    userService: UserService,
    authenticatorService: AuthenticatorService[JWTAuthenticator],
    eventBus: EventBus
  ): Environment[MyEnv] = {

    Environment[MyEnv](
      userService,
      authenticatorService,
      Seq(),
      eventBus
    )
  }

  @Provides
  def provideSocialProviderRegistry( //      facebookProvider: FacebookProvider,
  //      googleProvider: GoogleProvider,
  //      vkProvider: VKProvider,
  //      twitterProvider: TwitterProvider,
  //      xingProvider: XingProvider
  ): SocialProviderRegistry = {

    SocialProviderRegistry(Seq())
  }
  /**
   * Provides the crypter for the OAuth1 token secret provider.
   *
   * @param configuration The Play configuration.
   * @return The crypter for the OAuth1 token secret provider.
   */
  @Provides @Named("oauth1-token-secret-crypter")
  def provideOAuth1TokenSecretCrypter(configuration: Configuration): Crypter = {
    val config = configuration.underlying.as[JcaCrypterSettings]("silhouette.oauth1TokenSecretProvider.crypter")

    new JcaCrypter(config)
  }

  /**
   * Provides the crypter for the authenticator.
   *
   * @param configuration The Play configuration.
   * @return The crypter for the authenticator.
   */
  @Provides @Named("authenticator-crypter")
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    val config = configuration.underlying.as[JcaCrypterSettings]("silhouette.authenticator.crypter")

    new JcaCrypter(config)
  }

  /**
   * Provides the auth info repository.
   *
   * @param passwordInfoDAO The implementation of the delegable password auth info DAO.
   * @param oauth1InfoDAO The implementation of the delegable OAuth1 auth info DAO.
   * @param oauth2InfoDAO The implementation of the delegable OAuth2 auth info DAO.
   * @param openIDInfoDAO The implementation of the delegable OpenID auth info DAO.
   * @return The auth info repository instance.
   */
  @Provides
  def provideAuthInfoRepository(
    passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo],
    oauth1InfoDAO: DelegableAuthInfoDAO[OAuth1Info],
    oauth2InfoDAO: DelegableAuthInfoDAO[OAuth2Info],
    openIDInfoDAO: DelegableAuthInfoDAO[OpenIDInfo]
  ): AuthInfoRepository = {

    new DelegableAuthInfoRepository(passwordInfoDAO, oauth1InfoDAO, oauth2InfoDAO, openIDInfoDAO)
  }

  /**
   * Provides the authenticator service.
   *
   * @param crypter The crypter implementation.
   * @param idGenerator The ID generator implementation.
   * @param configuration The Play configuration.
   * @param clock The clock instance.
   * @return The authenticator service.
   */
  @Provides
  def provideAuthenticatorService(
    @Named("authenticator-crypter") crypter: Crypter,
    idGenerator: IDGenerator,
    configuration: Configuration,
    clock: Clock
  ): AuthenticatorService[JWTAuthenticator] = {

    val config = configuration.underlying.as[JWTAuthenticatorSettings]("silhouette.authenticator")
    val encoder = new CrypterAuthenticatorEncoder(crypter)

    new JWTAuthenticatorService(config, None, encoder, idGenerator, clock)
  }

  /**
   * Provides the avatar service.
   *
   * @param httpLayer The HTTP layer implementation.
   * @return The avatar service implementation.
   */
  @Provides
  def provideAvatarService(httpLayer: HTTPLayer): AvatarService = new GravatarService(httpLayer)

  /**
   * Provides the password hasher registry.
   *
   * @param passwordHasher The default password hasher implementation.
   * @return The password hasher registry.
   */
  @Provides
  def providePasswordHasherRegistry(passwordHasher: PasswordHasher): PasswordHasherRegistry = {
    new PasswordHasherRegistry(passwordHasher)
  }

  /**
   * Provides the credentials provider.
   *
   * @param authInfoRepository The auth info repository implementation.
   * @param passwordHasherRegistry The password hasher registry.
   * @return The credentials provider.
   */
  @Provides
  def provideCredentialsProvider(
    authInfoRepository: AuthInfoRepository,
    passwordHasherRegistry: PasswordHasherRegistry
  ): CredentialsProvider = {

    new CredentialsProvider(authInfoRepository, passwordHasherRegistry)
  }

  //
  //  /**
  //   * Provides the Facebook provider.
  //   *
  //   * @param httpLayer The HTTP layer implementation.
  //   * @param stateProvider The OAuth2 state provider implementation.
  //   * @param configuration The Play configuration.
  //   * @return The Facebook provider.
  //   */
  //  @Provides
  //  def provideFacebookProvider(
  //    httpLayer: HTTPLayer,
  //    stateProvider: OAuth2StateProvider,
  //    configuration: Configuration
  //  ): FacebookProvider = {
  //
  //    new FacebookProvider(httpLayer, stateProvider, configuration.underlying.as[OAuth2Settings]("silhouette.facebook"))
  //  }

  //  /**
  //   * Provides the Google provider.
  //   *
  //   * @param httpLayer The HTTP layer implementation.
  //   * @param stateProvider The OAuth2 state provider implementation.
  //   * @param configuration The Play configuration.
  //   * @return The Google provider.
  //   */
  //  @Provides
  //  def provideGoogleProvider(
  //    httpLayer: HTTPLayer,
  //    stateProvider: OAuth2StateProvider,
  //    configuration: Configuration
  //  ): GoogleProvider = {
  //
  //    new GoogleProvider(httpLayer, stateProvider, configuration.underlying.as[OAuth2Settings]("silhouette.google"))
  //  }
  //
  //  /**
  //   * Provides the VK provider.
  //   *
  //   * @param httpLayer The HTTP layer implementation.
  //   * @param stateProvider The OAuth2 state provider implementation.
  //   * @param configuration The Play configuration.
  //   * @return The VK provider.
  //   */
  //  @Provides
  //  def provideVKProvider(
  //    httpLayer: HTTPLayer,
  //    stateProvider: OAuth2StateProvider,
  //    configuration: Configuration
  //  ): VKProvider = {
  //
  //    new VKProvider(httpLayer, stateProvider, configuration.underlying.as[OAuth2Settings]("silhouette.vk"))
  //  }

  //  /**
  //   * Provides the Twitter provider.
  //   *
  //   * @param httpLayer The HTTP layer implementation.
  //   * @param tokenSecretProvider The token secret provider implementation.
  //   * @param configuration The Play configuration.
  //   * @return The Twitter provider.
  //   */
  //  @Provides
  //  def provideTwitterProvider(
  //    httpLayer: HTTPLayer,
  //    tokenSecretProvider: OAuth1TokenSecretProvider,
  //    configuration: Configuration
  //  ): TwitterProvider = {
  //
  //    val settings = configuration.underlying.as[OAuth1Settings]("silhouette.twitter")
  //    new TwitterProvider(httpLayer, new PlayOAuth1Service(settings), tokenSecretProvider, settings)
  //  }
  //
  //  /**
  //   * Provides the Xing provider.
  //   *
  //   * @param httpLayer The HTTP layer implementation.
  //   * @param tokenSecretProvider The token secret provider implementation.
  //   * @param configuration The Play configuration.
  //   * @return The Xing provider.
  //   */
  //  @Provides
  //  def provideXingProvider(
  //    httpLayer: HTTPLayer,
  //    tokenSecretProvider: OAuth1TokenSecretProvider,
  //    configuration: Configuration
  //  ): XingProvider = {
  //
  //    val settings = configuration.underlying.as[OAuth1Settings]("silhouette.xing")
  //    new XingProvider(httpLayer, new PlayOAuth1Service(settings), tokenSecretProvider, settings)
  //  }
}