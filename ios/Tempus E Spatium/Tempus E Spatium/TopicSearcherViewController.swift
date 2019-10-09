//
//  TopicSearcherViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import DropDown
import Alamofire
import Ono

extension DataRequest {
    static func xmlResponseSerializer() -> DataResponseSerializer<ONOXMLDocument> {
        return DataResponseSerializer { request, response, data, error in
            // Pass through any underlying URLSession error to the .network case.
            guard error == nil else { return .failure(BackendError.network(error: error!)) }

            // Use Alamofire's existing data serializer to extract the data, passing the error as nil, as it has
            // already been handled.
            let result = Request.serializeResponseData(response: response, data: data, error: nil)

            guard case let .success(validData) = result else {
                return .failure(BackendError.dataSerialization(error: result.error! as! AFError))
            }

            do {
                let xml = try ONOXMLDocument(data: validData)
                return .success(xml)
            } catch {
                return .failure(BackendError.xmlSerialization(error: error))
            }
        }
    }

    @discardableResult
    func responseXMLDocument(
        queue: DispatchQueue? = nil,
        completionHandler: @escaping (DataResponse<ONOXMLDocument>) -> Void)
        -> Self
    {
        return response(
            queue: queue,
            responseSerializer: DataRequest.xmlResponseSerializer(),
            completionHandler: completionHandler
        )
    }
}


class TopicSearcherViewController: UIViewController {
    
    @IBOutlet weak var mSubmitAndPlayBtn: UIButton!
    
    @IBAction func mSubmitAndPlayClicked() {
        if mSubmitAndPlayBtn.state != .disabled {
            performSegue(withIdentifier: "launchRound1", sender: nil)
        }
    }
    
    
    @IBOutlet weak var mLocaleDropDown: DropDown!
    
    @IBOutlet weak var mTopicDropDown: DropDown!
    
    @IBOutlet weak var mSelectAnyHeader: UILabel!
    
    func english() {
        AF.request("\(mBaseUrl)BluetoothSyncAttendance", method: .get, parameters: [:], encoding: JSONEncoding.default)
            .validate(statusCode: 200..<300)
            .validate(contentType: ["application/json"])
            .responseData { response in
                switch response.result {
                case .success(let value):
                    
                    guard let url = Bundle.main.url(forResource: "nutrition", withExtension: "html"),
                        let data = try? Data(contentsOf: url) else
                    {
                        fatalError("Missing resource: nutrition.xml")
                    }
                    
                    let document = try ONOXMLDocument(data: data)
                    document.rootElement.tag
                    
                    for element in document.rootElement.children.first?.children ?? [] {
                        let nutrient = element.tag
                        let amount = element.numberValue!
                        let unit = element.attributes["units"]!
                        
                        print("- \(amount)\(unit) \(nutrient)")
                    }
                    
                    document.enumerateElements(withXPath: "//food/name") { (element, _, _) in
                        print(element)
                    }
                case .failure(let error):
                    break
                }
        }
    }
    
    func francais() {
        
    }
    
    func espanol() {
        
    }
    
    func deutsch() {
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mSelectAnyHeader.text = NSLocalizedString("topic_searcher_heading", comment: "")
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "launchRound1":
            let destinationVC = segue.destination as! Round1ViewController
        default:
            break
        }
    }
    
}
