#!/usr/bin/env python3
"""
Genera el grafico VUs vs peticiones por segundo a partir de la salida
JSON de k6 (k6 run --out json=results/metrics.json ...).

Uso:
    python3 scripts/plot_vus_vs_reqs.py results/metrics.json results/vus_vs_reqs.png

Requiere: matplotlib  (pip install matplotlib)

El formato --out json de k6 emite una linea JSON por muestra:
    {"type":"Point","metric":"vus","data":{"time":"...","value":140,...}}
    {"type":"Point","metric":"http_reqs","data":{"time":"...","value":1,...}}
"""
import json
import sys
from collections import defaultdict
from datetime import datetime


def parse(path):
    # VUs: se toma el ultimo valor por segundo (gauge).
    # http_reqs: se cuentan las peticiones por segundo (contador de eventos).
    vus_by_sec = {}
    reqs_by_sec = defaultdict(int)

    with open(path, "r") as fh:
        for line in fh:
            line = line.strip()
            if not line:
                continue
            try:
                obj = json.loads(line)
            except json.JSONDecodeError:
                continue
            if obj.get("type") != "Point":
                continue
            metric = obj.get("metric")
            data = obj.get("data", {})
            ts = data.get("time")
            if not ts:
                continue
            # Trunca al segundo.
            try:
                t = datetime.fromisoformat(ts.replace("Z", "+00:00"))
            except ValueError:
                continue
            sec = t.replace(microsecond=0)

            if metric == "vus":
                vus_by_sec[sec] = data.get("value", 0)
            elif metric == "http_reqs":
                reqs_by_sec[sec] += data.get("value", 0)

    return vus_by_sec, reqs_by_sec


def main():
    if len(sys.argv) < 3:
        print("Uso: plot_vus_vs_reqs.py <metrics.json> <salida.png>")
        sys.exit(1)

    in_path, out_path = sys.argv[1], sys.argv[2]
    vus_by_sec, reqs_by_sec = parse(in_path)

    if not vus_by_sec and not reqs_by_sec:
        print("No se encontraron muestras 'vus' ni 'http_reqs' en el JSON.")
        sys.exit(2)

    import matplotlib
    matplotlib.use("Agg")
    import matplotlib.pyplot as plt
    import matplotlib.dates as mdates

    secs = sorted(set(list(vus_by_sec.keys()) + list(reqs_by_sec.keys())))
    vus_series = [vus_by_sec.get(s, 0) for s in secs]
    reqs_series = [reqs_by_sec.get(s, 0) for s in secs]

    fig, ax1 = plt.subplots(figsize=(12, 5))

    ax1.set_xlabel("Tiempo")
    ax1.set_ylabel("VUs", color="tab:green")
    ax1.plot(secs, vus_series, color="tab:green", label="VUs")
    ax1.tick_params(axis="y", labelcolor="tab:green")

    ax2 = ax1.twinx()
    ax2.set_ylabel("Peticiones por segundo", color="tab:blue")
    ax2.plot(secs, reqs_series, color="tab:blue", label="http_reqs/s")
    ax2.tick_params(axis="y", labelcolor="tab:blue")

    ax1.xaxis.set_major_formatter(mdates.DateFormatter("%H:%M"))
    fig.autofmt_xdate()
    plt.title("VUs vs peticiones por segundo")
    fig.tight_layout()
    plt.savefig(out_path, dpi=120)
    print(f"Grafico guardado en: {out_path}")


if __name__ == "__main__":
    main()
