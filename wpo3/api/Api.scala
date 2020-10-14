package api

import domain.{Domain}

trait Api
{
  this: Domain =>
  def manageUser: ManageUser
  def manageBike: ManageBike
}