// Copyright: 2017 - 2018 Sam Halliday
// License: http://www.gnu.org/licenses/gpl-3.0.en.html

package fommil
package dda

import prelude._, Z._

import java.lang.Throwable
import time.Epoch

// this entire file should either be autogenerated with codegen or a compiler
// plugin / macro on the companion (which would allow case classes to be final)

// scalafix:off
package algebra {

  import scala.language.higherKinds

  private[algebra] abstract class DroneBoilerplate {
    this: Drone.type =>

    def liftM[F[_]: Monad, G[_[_], _]: MonadTrans](
      f: Drone[F]
    ): Drone[G[F, ?]] =
      new Drone[G[F, ?]] {
        def getBacklog: G[F, Int] = f.getBacklog.liftM[G]
        def getAgents: G[F, Int]  = f.getAgents.liftM[G]
      }

    def liftIO[F[_]: Monad, E](
      io: Drone[IO[E, ?]]
    )(implicit M: MonadIO[F, E]): Drone[F] =
      new Drone[F] {
        def getBacklog: F[Int] = M.liftIO(io.getBacklog)
        def getAgents: F[Int]  = M.liftIO(io.getAgents)
      }

    def liftTask[F[_]: Monad](
      io: Drone[Task]
    )(implicit M: MonadIO[F, Throwable]): Drone[F] =
      liftIO[F, Throwable](io)

    sealed abstract class Ast[A]
    case class GetBacklog() extends Ast[Int]
    case class GetAgents()  extends Ast[Int]

    def liftF[F[_]](implicit I: Ast :<: F): Drone[Free[F, ?]] =
      new Drone[Free[F, ?]] {
        def getBacklog: Free[F, Int] = Free.liftF(I.inj(GetBacklog()))
        def getAgents: Free[F, Int]  = Free.liftF(I.inj(GetAgents()))
      }

    def liftA[F[_]](implicit I: Ast :<: F): Drone[FreeAp[F, ?]] =
      new Drone[FreeAp[F, ?]] {
        def getBacklog: FreeAp[F, Int] = FreeAp.lift(I.inj(GetBacklog()))
        def getAgents: FreeAp[F, Int]  = FreeAp.lift(I.inj(GetAgents()))
      }

    def liftCoyo[F[_]](implicit I: Ast :<: F): Drone[Coyoneda[F, ?]] =
      new Drone[Coyoneda[F, ?]] {
        def getBacklog: Coyoneda[F, Int] = Coyoneda.lift(I.inj(GetBacklog()))
        def getAgents: Coyoneda[F, Int]  = Coyoneda.lift(I.inj(GetAgents()))
      }

    def interpreter[F[_]](f: Drone[F]): Ast ~> F = λ[Ast ~> F] {
      case GetBacklog() => f.getBacklog: F[Int]
      case GetAgents()  => f.getAgents: F[Int]
    }

  }

  private[algebra] abstract class MachinesBoilerlate {
    this: Machines.type =>

    def liftM[F[_]: Monad, G[_[_], _]: MonadTrans](
      f: Machines[F]
    ): Machines[G[F, ?]] =
      new Machines[G[F, ?]] {
        def getTime: G[F, Epoch]                        = f.getTime.liftM[G]
        def getManaged: G[F, NonEmptyList[MachineNode]] = f.getManaged.liftM[G]
        def getAlive: G[F, MachineNode ==>> Epoch]      = f.getAlive.liftM[G]
        def start(node: MachineNode): G[F, Unit]        = f.start(node).liftM[G]
        def stop(node: MachineNode): G[F, Unit]         = f.stop(node).liftM[G]
      }

    def liftIO[F[_]: Monad, E](
      io: Machines[IO[E, ?]]
    )(implicit M: MonadIO[F, E]): Machines[F] = new Machines[F] {
      def getTime: F[Epoch]                        = M.liftIO(io.getTime)
      def getManaged: F[NonEmptyList[MachineNode]] = M.liftIO(io.getManaged)
      def getAlive: F[MachineNode ==>> Epoch]      = M.liftIO(io.getAlive)
      def start(node: MachineNode): F[Unit]        = M.liftIO(io.start(node))
      def stop(node: MachineNode): F[Unit]         = M.liftIO(io.stop(node))
    }

    def liftTask[F[_]: Monad](
      io: Machines[Task]
    )(implicit M: MonadIO[F, Throwable]): Machines[F] =
      liftIO[F, Throwable](io)

    sealed abstract class Ast[A]
    case class GetTime()                extends Ast[Epoch]
    case class GetManaged()             extends Ast[NonEmptyList[MachineNode]]
    case class GetAlive()               extends Ast[MachineNode ==>> Epoch]
    case class Start(node: MachineNode) extends Ast[Unit]
    case class Stop(node: MachineNode)  extends Ast[Unit]

    def liftF[F[_]](implicit I: Ast :<: F): Machines[Free[F, ?]] =
      new Machines[Free[F, ?]] {
        def getTime: Free[F, Epoch] = Free.liftF(I.inj(GetTime()))
        def getManaged: Free[F, NonEmptyList[MachineNode]] =
          Free.liftF(I.inj(GetManaged()))
        def getAlive: Free[F, MachineNode ==>> Epoch] =
          Free.liftF(I.inj(GetAlive()))
        def start(node: MachineNode): Free[F, Unit] =
          Free.liftF(I.inj(Start(node)))
        def stop(node: MachineNode): Free[F, Unit] =
          Free.liftF(I.inj(Stop(node)))
      }

    def liftA[F[_]](implicit I: Ast :<: F): Machines[FreeAp[F, ?]] =
      new Machines[FreeAp[F, ?]] {
        def getTime: FreeAp[F, Epoch] = FreeAp.lift(I.inj(GetTime()))
        def getManaged: FreeAp[F, NonEmptyList[MachineNode]] =
          FreeAp.lift(I.inj(GetManaged()))
        def getAlive: FreeAp[F, MachineNode ==>> Epoch] =
          FreeAp.lift(I.inj(GetAlive()))
        def start(node: MachineNode): FreeAp[F, Unit] =
          FreeAp.lift(I.inj(Start(node)))
        def stop(node: MachineNode): FreeAp[F, Unit] =
          FreeAp.lift(I.inj(Stop(node)))
      }

    def liftCoyo[F[_]](implicit I: Ast :<: F): Machines[Coyoneda[F, ?]] =
      new Machines[Coyoneda[F, ?]] {
        def getTime: Coyoneda[F, Epoch] = Coyoneda.lift(I.inj(GetTime()))
        def getManaged: Coyoneda[F, NonEmptyList[MachineNode]] =
          Coyoneda.lift(I.inj(GetManaged()))
        def getAlive: Coyoneda[F, MachineNode ==>> Epoch] =
          Coyoneda.lift(I.inj(GetAlive()))
        def start(node: MachineNode): Coyoneda[F, Unit] =
          Coyoneda.lift(I.inj(Start(node)))
        def stop(node: MachineNode): Coyoneda[F, Unit] =
          Coyoneda.lift(I.inj(Stop(node)))
      }

    def interpreter[F[_]](f: Machines[F]): Ast ~> F = λ[Ast ~> F] {
      case GetTime()    => f.getTime: F[Epoch]
      case GetManaged() => f.getManaged: F[NonEmptyList[MachineNode]]
      case GetAlive()   => f.getAlive: F[MachineNode ==>> Epoch]
      case Start(node)  => f.start(node): F[Unit]
      case Stop(node)   => f.stop(node): F[Unit]
    }

  }
}

package logic {

  import scala.language.higherKinds

  private[logic] abstract class DynAgentsBoilerplate {
    this: DynAgents.type =>

    def liftM[F[_]: Monad, G[_[_], _]: MonadTrans](
      f: DynAgents[F]
    ): DynAgents[G[F, ?]] =
      new DynAgents[G[F, ?]] {
        def initial: G[F, WorldView]                = f.initial.liftM[G]
        def update(old: WorldView): G[F, WorldView] = f.update(old).liftM[G]
        def act(world: WorldView): G[F, WorldView]  = f.act(world).liftM[G]
      }

    def liftIO[F[_]: Monad, E](
      io: DynAgents[IO[E, ?]]
    )(implicit M: MonadIO[F, E]): DynAgents[F] =
      new DynAgents[F] {
        def initial: F[WorldView]                = M.liftIO(io.initial)
        def update(old: WorldView): F[WorldView] = M.liftIO(io.update(old))
        def act(world: WorldView): F[WorldView]  = M.liftIO(io.act(world))
      }

    def liftTask[F[_]: Monad](
      io: DynAgents[Task]
    )(implicit M: MonadIO[F, Throwable]): DynAgents[F] =
      liftIO[F, Throwable](io)

  }

}
