export const buttonClasses = {
  primary:
    "rounded-lg bg-blue-600 px-4 py-2.5 font-medium text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50 disabled:hover:bg-blue-600",
  secondary:
    "rounded-lg border border-slate-300 bg-white px-4 py-2.5 font-medium text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-50",
  danger:
    "rounded-lg bg-red-500 px-4 py-2.5 font-medium text-white transition hover:bg-red-600 disabled:cursor-not-allowed disabled:opacity-50"
}

export const inputBaseClasses =
  "w-full rounded-lg border px-3 py-2.5 text-slate-800 outline-none transition"

export const inputStateClasses = {
  normal: "border-slate-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-200",
  error: "border-red-300 bg-red-50 focus:border-red-500 focus:ring-2 focus:ring-red-200"
}

export const alertClasses = {
  success: "rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700",
  error: "rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700",
  warning: "rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-700",
  info: "rounded-lg border border-blue-200 bg-blue-50 px-4 py-3 text-sm text-blue-700"
}