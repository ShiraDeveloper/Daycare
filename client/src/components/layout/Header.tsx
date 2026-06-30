import { useAuth } from '../../context/AuthContext'

export function Header() {
  const { logout } = useAuth()
  const today = new Date().toLocaleDateString(undefined, {
    weekday: 'long',
    month: 'long',
    day: 'numeric',
  })

  return (
    <header className="border-b border-border bg-white/80 backdrop-blur-md">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4 sm:px-6">
        <div>
          <p className="text-xs font-semibold uppercase tracking-widest text-brand-600">
            Daycare Educator
          </p>
          <h1 className="text-lg font-bold text-slate-900 sm:text-xl">
            Today&apos;s Board
          </h1>
          <p className="text-sm text-muted">{today}</p>
        </div>
        <button
          type="button"
          onClick={logout}
          className="rounded-xl border border-border px-4 py-2 text-sm font-medium text-slate-600 transition hover:bg-slate-50"
        >
          Sign out
        </button>
      </div>
    </header>
  )
}
