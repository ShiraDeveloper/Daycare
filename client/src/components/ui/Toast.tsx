import type { ReactNode } from 'react'

interface ToastProps {
  message: string
  visible: boolean
  type?: 'success' | 'error'
}

export function Toast({ message, visible, type = 'success' }: ToastProps) {
  if (!visible) return null

  const styles =
    type === 'success'
      ? 'bg-brand-700 text-white'
      : 'bg-red-600 text-white'

  return (
    <div
      className={`fixed bottom-6 left-1/2 z-[60] -translate-x-1/2 animate-[slideUp_0.3s_ease-out] rounded-full px-6 py-3 text-sm font-medium shadow-lg ${styles}`}
      role="status"
    >
      {message}
    </div>
  )
}

export function StatusBadge({ children }: { children: ReactNode }) {
  return (
    <span className="inline-flex items-center rounded-full bg-brand-100 px-2.5 py-0.5 text-xs font-semibold text-brand-800">
      {children}
    </span>
  )
}
