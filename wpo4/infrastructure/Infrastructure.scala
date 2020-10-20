package infrastructure

import api.{ManageBike, ManageBikeTrait, ManageUser, ManageUserTrait}
import com.google.inject.{Inject, Injector}

class Infrastructure @Inject() (var manageUser: ManageUserTrait, var manageBike: ManageBikeTrait, injector: Injector){
  manageUser = injector.getInstance(classOf[ManageUser])
  manageBike = injector.getInstance(classOf[ManageBike])
}
