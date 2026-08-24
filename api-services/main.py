import os
import subprocess

import requests
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException, status
from pydantic import BaseModel

load_dotenv()

API_KEY = os.getenv("API_KEY")

app = FastAPI()


class DomainScanRequest(BaseModel):
    domain: str


class NmapScanRequest(BaseModel):
    target: str


@app.post("/domainScan")
def scan_domain(scan_data: DomainScanRequest):

    if not API_KEY:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="VirusTotal API key is not configured in the environment."
        )

    target_domain = scan_data.domain

    url = f"https://virustotal.com/api/v3/domains/{target_domain}"

    headers = {
        "accept": "application/json",
        "x-apikey": API_KEY
    }

    try:
        response = requests.get(
            url,
            headers=headers,
            timeout=10
        )

        if response.status_code != 200:
            raise HTTPException(
                status_code=response.status_code,
                detail=response.json().get(
                    "error", {}
                ).get(
                    "message",
                    "Lookup failed"
                )
            )

        raw_data = response.json().get("data", {})
        attributes = raw_data.get("attributes", {})
        stats = attributes.get("last_analysis_stats", {})

        clean_payload = {
            "domain": target_domain,
            "reputation": attributes.get("reputation", 0),
            "stats": stats,
            "is_flagged": (
                stats.get("malicious", 0)
                + stats.get("suspicious", 0)
            ) > 0,
            "registrar": attributes.get("registrar", "Unknown"),
            "creation_date": attributes.get("creation_date"),
            "categories": attributes.get("categories", {}),
            "dns_records": [
                {
                    "type": record.get("type"),
                    "value": record.get("value"),
                    "ttl": record.get("ttl")
                }
                for record in attributes.get(
                    "last_dns_records", []
                )
            ]
        }

        return {"response": clean_payload}

    except requests.exceptions.Timeout:
        raise HTTPException(
            status_code=status.HTTP_504_GATEWAY_TIMEOUT,
            detail="VirusTotal request timed out."
        )

    except requests.exceptions.RequestException as error:
        raise HTTPException(
            status_code=status.HTTP_502_BAD_GATEWAY,
            detail=f"Network error: {str(error)}"
        )


@app.post("/nmapScan")
def nmap_scan(scan_data: NmapScanRequest):

    try:
        results = subprocess.run(
            ["nmap", scan_data.target],
            capture_output=True,
            text=True,
            check=True
        )

        return {
            "target": scan_data.target,
            "result": results.stdout
        }

    except subprocess.CalledProcessError as error:
        raise HTTPException(
            status_code=500,
            detail=error.stderr or "Nmap scan failed"
        )