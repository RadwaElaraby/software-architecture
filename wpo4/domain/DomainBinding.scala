package domain

import com.google.inject.{Binder ,Module}

class DomainBinding extends Module {
  def configure ( binder : Binder ) = {
    binder.bind ( classOf [ UserTrait ]).to( classOf [ UserRepository])
    binder.bind ( classOf [ BikeTrait ]).to( classOf [ BikeRepository])
  }
}