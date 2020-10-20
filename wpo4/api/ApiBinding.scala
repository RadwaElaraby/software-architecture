package api

import com.google.inject.{Binder ,Module}

class ApiBinding extends Module {
  def configure ( binder : Binder ) = {
    binder.bind ( classOf [ ManageUserTrait ]).to( classOf [ ManageUser])
    binder.bind ( classOf [ ManageBikeTrait ]).to( classOf [ ManageBike])
  }
}